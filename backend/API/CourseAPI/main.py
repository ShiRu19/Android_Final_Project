from flask import *
import CourseAPI
import json
import os

app = Flask(__name__)


@app.route("/courseTable")
def getCourseTable():
    username = request.args["username"]
    password = request.args["password"]
    studentID = int(request.args["studentID"])
    year = request.args["year"]
    sem = request.args["sem"]
    data = CourseAPI.fetchCourseData(username, password, studentID, year, sem)
    return Response(json.dumps(data), mimetype="application/json")


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))