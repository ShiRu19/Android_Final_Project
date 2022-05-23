# School Epidemic Report System

## Introduce

一款疫情通報系統，可以讓學校組織內的確診者使用這款系統進行通報，且管理者可以確實知道確診者，做出立即的應對措施。



主要分成行政端與學生端，行政端可以審核學生的確診足跡，並且回朔先前的課表來製造確診足跡資料。

學生端可以進行登入，供給行政端進行審核，方便整合確診足跡。



## Author

- 國立臺北科技大學　資財四甲　黃詩洳
- 國立臺北科技大學　資財四甲　許景雲
- 國立臺北科技大學　資工二　　[黃漢軒](https://ntut-xuan.github.io)



## ChangeLog

- 2022 / 05 / 09
  - 將 API 系統建置完成 (Xuan)

- 2022 / 05 / 20
  - 完成了 Bottom Navigation 的建置 (Xuan)

- 2022 / 05 / 22
  - 完成了 APP 起始封面建置 (Xuan)
  - ~~完成了 Google OAuth 登入的建置 (Xuan)~~（**停止支援了，所以拋棄該方法**）
  - 完成校園入口網站登入 Web Data API，可以藉由 POST 帳號與密碼得到信箱、身份與名稱 (Xuan)
  - 校園入口網站登入 Web Data API 建置到 Cloud Run 上 (Xuan)

- 2022 / 05 / 23
  - 新增了精美的 [Alert](https://github.com/Tapadoo/Alerter) (Xuan)
  - 完成手機上的登入建置 (Xuan)
  - 撰寫好 remember me (Xuan)
  

