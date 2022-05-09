# School Course API

## 校園課表 API

主要供給這份專案進行特定學號的校園課表查詢。



## 後端建立技術

- 使用 Flask 進行網站的建置。
- 使用 Requests 與 Beautifulsoup4 來進行資料處理。



## 回傳結果

- status：用來告知 API 回傳結果是否出現問題。
  - OK：查詢成功，並且帶有一標籤 data 為該學號的課表。
  - Error：查詢失敗，系統無法正確的處理資料，帶有一標籤 message 為系統輸出的錯誤訊息。

- data：用來回傳課表的資料，為一 JSON 陣列，**每一陣列的元素以課號為 KEY 值**。

  |      標籤       |            意義             |    型態     |
  | :-------------: | :-------------------------: | :---------: |
  |   courseName    |          課程名稱           | 字串 String |
  |  courseTeacher  |          課程老師           |  字串陣列   |
  |   courseClass   |          開課單位           |  字串陣列   |
  | courseClassroom |          課程教室           |  字串陣列   |
  |   courseTime    | 開課時間，形式為：星期-節次 |  字串陣列   |

  