import mimetypes
import CourseAPI
import json
import os
import time
from datetime import datetime, timedelta

import firebase_admin
import requests
from firebase_admin import credentials, auth
from firebase_admin import firestore
from flask import Flask, request, Response, send_from_directory, session, redirect

import CourseAPI

app = Flask(__name__, static_url_path='')
firebase_app = firebase_admin.initialize_app(credentials.Certificate("./serviceAccountKey.json"))
db = firestore.client(firebase_app)
app.config['JSON_SORT_KEYS'] = False
app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=31)
app.secret_key = os.urandom(16).hex()

firebaseTokenMap = {}

_verify_token_url = 'https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyCustomToken'


def _sign_in(custom_token, api_key):
    body = {'token': custom_token.decode(), 'returnSecureToken': True}
    params = {'key': api_key}
    resp = requests.request('post', _verify_token_url, params=params, json=body)
    resp.raise_for_status()
    return resp.json().get('idToken')


def test_custom_token(api_key):
    custom_token = auth.create_custom_token('user1')
    id_token = _sign_in(custom_token, api_key)
    claims = auth.verify_id_token(id_token)
    assert claims['uid'] == 'user1'


@app.route("/static/<path:path>")
def returnStaticFile(path):
    return send_from_directory('./static/', path)


@app.route("/login", methods=["GET", "POST"])
def loginNTUT():
    username = request.args["username"]
    password = request.args["password"]

    host = "https://app.ntut.edu.tw/"
    login_url = host + "login.do"
    photo_url = host + "photoView.do"
    data = {"muid": username, "mpassword": password, "forceMobile": "app", "md5Code": "1111", "ssoId": ""}
    header = {"User-Agent": "Direk Android App"}
    res = requests.post(login_url, data=data, headers=header)
    student_data = json.loads(res.text)
    response_data = {"status": "ok"}

    if not student_data["success"]:
        response_data["status"] = "failed"
        response_data["message"] = student_data["errorMsg"]
    else:
        response_data["status"] = "ok"
        response_data["data"] = {}
        response_data["data"]["studentName"] = student_data["givenName"]
        response_data["data"]["studentEmail"] = student_data["userMail"]
        response_data["data"]["studentRole"] = student_data["userRole"] if username not in {"109590031", "107AB0008", "107AB0018"} else "T"
        response_data["data"]["userPhoto"] = student_data["userPhoto"]
        response_data["data"]["firebaseToken"] = auth.create_custom_token(username).decode("utf-8")

        firebaseTokenMap[username] = response_data["data"]["firebaseToken"]
        sessionID = os.urandom(16).hex()

    if "filename" in request.args:
        filename = request.args["filename"]
        res = requests.post(photo_url, data={"realname": filename}, headers=header, cookies=res.cookies, stream=True)
        return Response(res.raw, mimetype="image/jpeg")

    if "year" in request.args and "sem" in request.args:
        year = request.args["year"]
        sem = request.args["sem"]
        response_data["data"]["studentCourse"] = CourseAPI.fetchCourseData(username, password, username, year, sem)
        db.collection(u'user').document(u'109590099').set({"course": response_data["data"]["studentCourse"]})

    resp = Response(json.dumps(response_data), mimetype="application/json")

    if student_data["success"]:
        sessionID = os.urandom(16).hex()
        resp.set_cookie("SID", value=sessionID, expires=time.time() + 24 * 60 * 60)
        session[sessionID] = {"username": username}

    return resp


@app.route("/admin_login", methods=["GET", "POST"])
def admin_login_page():
    index_html = open("./html/login.html", "r", encoding="utf8")
    return index_html.read()


@app.route("/admin_logout", methods=["POST"])
def admin_logout():
    if request.cookies.get('SID') is not None or request.cookies.get('SID') in session:
        SID = request.cookies.get('SID')
        del session[SID]
    return redirect("/admin_login")


@app.route("/admin_index", methods=["GET"])
def admin_index_page():
    if request.cookies.get('SID') is None or request.cookies.get('SID') not in session:
        return redirect("/admin_login")

    index_html = open("./html/index.html", "r", encoding="utf8")
    return index_html.read()


@app.route("/admin_verdict", methods=["GET"])
def admin_verdict_page():
    if request.cookies.get('SID') is None or request.cookies.get('SID') not in session:
        return redirect("/admin_login")

    if "id" in request.args:
        index_html = open("./html/verdict_data.html", "r", encoding="utf8")
        return index_html.read()
    else:
        index_html = open("./html/verdict.html", "r", encoding="utf8")
        return index_html.read()

@app.route("/admin_statistic", methods=["GET"])
def admin_statistic_page():
    if request.cookies.get('SID') is None or request.cookies.get('SID') not in session:
        return redirect("/admin_login")

    index_html = open("./html/statistic.html", "r", encoding="utf8")
    return index_html.read()


@app.route("/admin_confirm", methods=["POST"])
def admin_confirm_post():
    response_data = {"status": "ok"}
    if request.cookies.get('SID') is None or request.cookies.get('SID') not in session:
        response_data["status"] = "Failed"
        response_data["message"] = "登入驗證失敗，請再次登入"
        return Response(json.dumps(response_data), mimetype="application/json")
    if "studentID" not in request.args:
        response_data["status"] = "Failed"
        response_data["message"] = "需輸入 studentID 參數。"
        return Response(json.dumps(response_data), mimetype="application/json")
    if "confirmWeekDay" not in request.args:
        response_data["status"] = "Failed"
        response_data["message"] = "需輸入 confirmWeekDay 參數。"
        return Response(json.dumps(response_data), mimetype="application/json")
    confirmWeekDay = int(request.args["confirmWeekDay"])
    studentID = request.args["studentID"]
    studentCourse = {}
    doc = db.collection(u'user').document(studentID).get()
    data = doc.to_dict()
    for course in data["course"]["data"]:
        courseID = course["courseID"]
        isolate = False
        for date in course["courseTime"]:
            date_string = str(date)
            day = int(date_string.split("-")[0])
            if abs(day - confirmWeekDay) < 3:
                isolate = True
        if isolate == False:
            continue
        current_date = datetime.now().replace(hour=0, minute=0, second=0, microsecond=0)
        studentCourse[courseID] = current_date.timestamp() * 1000
    db.collection(u'app').document(u'confirm_course').update(studentCourse)
    return Response(json.dumps(response_data), mimetype="application/json")
    


@app.route("/fetch_custom_token", methods=["POST"])
def fetch_custom_token():
    response_data = {"status": ""}
    if request.cookies.get('SID') is None or request.cookies.get('SID') not in session:
        response_data["status"] = "Failed"
        response_data["message"] = "驗證無效"
        return Response(json.dumps(response_data), mimetype="application/json")
    SID = request.cookies.get('SID')
    username = session[SID]["username"]
    response_data["status"] = "ok"
    response_data["data"] = {}
    response_data["data"]["custom_token"] = auth.create_custom_token(username).decode("utf-8")
    return Response(json.dumps(response_data), mimetype="application/json")


@app.route("/get_confirm_data", methods=["GET"])
def getConfirmDate():
    docs = db.collection(u'report').order_by(u"ReportTime", direction=firestore.Query.DESCENDING).where(u'AdministratorVerdict', u'==', 1).stream()
    response_data = {"status": "OK", "data": []}
    for doc in docs:
        data_package = {}
        data = doc.to_dict()
        data_package["ID"] = doc.id.split("-")[-1]
        data_package["studentClass"] = data["StudentDepartment"]
        data_package["confirmDate"] = data["RapidTestDate"].timestamp() * 1000
        response_data["data"].append(data_package)
    print(response_data)
    return Response(json.dumps(response_data), mimetype="application/json")


@app.route("/get_confirm_course", methods=["GET"])
def getConfirmCourse():
    docs = db.collection(u'app').document(u'confirm_course').get()
    response_data = {"status": "OK", "data": docs.to_dict()}
    return Response(json.dumps(response_data), mimetype="application/json")

if __name__ == "__main__":
    test_custom_token("AIzaSyBUJBnbG2uyzHtTo0gWNHuNocfUMuXqxkU")
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
