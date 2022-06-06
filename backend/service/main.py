import CourseAPI
import requests
import json
import firebase_admin
import os

from flask_cors import cross_origin
from flask_session import Session
from datetime import datetime as dt, timedelta
from flask import Flask, request, Response, send_from_directory, session, redirect
from firebase_admin import credentials, auth
import time;

app = Flask(__name__, static_url_path='')
firebase_app = firebase_admin.initialize_app(credentials.Certificate("./serviceAccountKey.json"))
app.config['JSON_SORT_KEYS'] = False
app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=31)
app.secret_key = os.urandom(16).hex()


@app.route("/dist/<path:path>")
def returnStaticFile(path):
	return send_from_directory('./html/dist', path)


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
        sessionID = os.urandom(16).hex()

    if "filename" in request.args:
        filename = request.args["filename"]
        res = requests.post(photo_url, data={"realname": filename}, headers=header, cookies=res.cookies, stream=True)
        return Response(res.raw, mimetype="image/jpeg")
    
    if "year" in request.args and "sem" in request.args:
        year = request.args["year"]
        sem = request.args["sem"]
        response_data["data"]["studentCourse"] = CourseAPI.fetchCourseData(username, password, username, year, sem)

    resp = Response(json.dumps(response_data), mimetype="application/json")
    
    if student_data["success"]:
        sessionID = os.urandom(16).hex()
        resp.set_cookie("SID", value = sessionID, expires=time.time()+24*60*60)
        session[sessionID] = {"username": username}
    
    return resp


@app.route("/admin_login", methods=["GET", "POST"])
def admin_login_page():
    index_html = open("./html/login.html", "r", encoding="utf8")
    return index_html.read()


@app.route("/admin_logout", methods=["POST"])
def admin_logout():
    if(request.cookies.get('SID') != None or request.cookies.get('SID') in session):
        SID = request.cookies.get('SID')
        del session[SID]
    return redirect("/admin_login")

@app.route("/admin_index", methods=["GET"])
def admin_index_page():

    if(request.cookies.get('SID') == None or request.cookies.get('SID') not in session):
        return redirect("/admin_login")
    
    index_html = open("./html/index.html", "r", encoding="utf8")
    return index_html.read()

'''
@app.route("/confirmReport", methods=["POST"])
def confirmReport():
    imageFile = request.files.get('positiveImage', '')
    imageFile.save("./image.jpg")
    imageFile.close()
    print(type(imageFile))
    print(request.form.keys())
    response_data = {"status": "OK", "message": "OK."}
    return Response(json.dumps(response_data), mimetype="application/json")
'''

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
