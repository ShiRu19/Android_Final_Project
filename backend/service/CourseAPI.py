import requests
import datetime
from bs4 import BeautifulSoup
import traceback

def fetchCourseData(username, password, studentID, year, sem):

    try:

        host = "https://app.ntut.edu.tw/"
        login_url = host + "login.do"
        sso_login_url = host + "ssoIndex.do"
        course_url = "https://aps.ntut.edu.tw/course/tw/courseSID.jsp"

        # 進行登入，取得登入 cookie（Post）
        data = {"muid": username, "mpassword": password, "forceMobile": "app", "md5Code": "1111", "ssoId": ""}
        header = {"User-Agent": "Direk Android App"}
        res = requests.post(login_url, data=data, headers=header)

        # 進行 ssoIndex 的驗證（Post）
        time = datetime.datetime.now().timestamp() * 1000
        data = {"apUrl": "https://aps.ntut.edu.tw/course/tw/courseSID.jsp", "apOu": "aa_0010-", "sso": "true", "datetime1": str(round(time))}
        res = requests.post(sso_login_url, data=data, headers=header, cookies=res.cookies)
        soup = BeautifulSoup(res.text, 'html.parser')

        # 要挖 secret data，然後砸到等等要 request 的網站上
        sessionId = soup.html.find_all("input")[0]['value']
        reqFrom = soup.html.find_all("input")[1]['value']
        userid = soup.html.find_all("input")[2]['value']
        userType = soup.html.find_all("input")[3]['value']

        # 砸到 https://aps.ntut.edu.tw/course/tw/courseSID.jsp
        data = {"sessionId": sessionId, "reqFrom": reqFrom, "userid": userid, "userType": userType}
        res = requests.post(course_url, data=data, headers=header, cookies=res.cookies)

        # 砸到 https://aps.ntut.edu.tw/course/tw/Select.jsp，呈現個人最新課表
        data = {"code": studentID, "format": "-2", "year": year, "sem": sem}
        res = requests.post("https://aps.ntut.edu.tw/course/tw/Select.jsp", data=data, headers=header, cookies=res.cookies)
        soup = BeautifulSoup(res.text, 'lxml')
        course_array = soup.html.find_all("a")

        #print(soup.prettify())

        course_list = []
        table = soup.html.find_all("table")[1].findChildren("tr")

        studentInfo = table[0].findChildren("td")[0].contents[0]
        studentDepartment = studentInfo.split("　　")[2].split("：")[1]

        for i in range(3, len(table)-1):
            tag = [7, 1, 2, 3, 4, 5, 6]
            courseCode = table[i].findChildren("td")[0].findChildren("a")[0].contents[0]
            courseName = table[i].findChildren("td")[1].findChildren("a")[0].contents[0]
            courseTeacher = [data.contents[0] for data in table[i].findChildren("td")[6].findChildren("a")]
            courseClass = [data.contents[0] for data in table[i].findChildren("td")[7].findChildren("a")]
            courseClassRoom = [data.contents[0] for data in table[i].findChildren("td")[15].findChildren("a")]
            course_list.append({"courseID": courseCode, "courseName": courseName, "courseTeacher": courseTeacher, "courseClass": courseClass, "courseClassroom": courseClassRoom, "courseTime": []})
            for j in range(7):
                classTime = table[i].findChildren("td")[8 + j].contents[0].strip()
                if len(classTime) > 0:
                    classTimeSplit = classTime.split()
                    for data in classTimeSplit:
                        course_list[len(course_list)-1]["courseTime"].append(str(tag[j]) + "-" + data)

        return {"status": "OK", "data": course_list, "studentDepartment": studentDepartment}

    except Exception as e:
        return {"status": "Error", "Message": traceback.format_exc()}
