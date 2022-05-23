from flask import Flask, request, Response
import requests
import json
import os

app = Flask(__name__)


@app.route("/login", methods=["POST"])
def loginNTUT():
    username = request.args["username"]
    password = request.args["password"]
    host = "https://app.ntut.edu.tw/"
    login_url = host + "login.do"
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
        response_data["data"]["studentRole"] = student_data["userRole"]

    return Response(json.dumps(response_data), mimetype="application/json")


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
