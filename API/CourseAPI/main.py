from flask import *
import CourseAPI
import json
import os

app = Flask(__name__)


@app.route("/courseTable")
def getCourseTable():
    studentID = int(request.args["studentID"])
    data = CourseAPI.fetchCourseData(studentID)
    return Response(json.dumps(data), mimetype="application/json")


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))