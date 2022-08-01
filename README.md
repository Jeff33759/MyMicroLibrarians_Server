# MyMicroLibrarians_Server
分散式架構實作館藏系統

<br><br>
## 專題大綱

市面上虛擬主機的發展已臻成熟，隨著雲端的使用愈趨普遍，加上前後端分離的趨勢，分散式的系統架構逐漸受到歡迎。

有別於傳統的主從式架構，分散式架構除了可以幫助人們採用更加彈性的雲端部屬方案外，也有著更高的容錯性，缺點就是管理與開發要來得更加複雜。

本專題利用Spring Boot實作了分散式系統的模型，希望在學習使用Spring Cloud之前，能夠藉由實作本專題，更加了解分散式系統的運行機制。

<br><br>
## 我做到了什麼

1. RESTful Api。

2. 使用Swagger套件，撰寫API文件

3. 捨棄基於Cookie的Session機制，改用JSON Web Token(JWT)來記錄使用者的狀態，方便不同伺服端之間共享使用者的公開資訊。

4. 客製化Spring Security的認證機制，實作自製的基於帳密的認證或是基於Token的認證邏輯。

5. Refresh Token與Access Token的實作。

6. Refresh Token Signature使用HS256對稱型加密，加密與解密都由同個伺服端負責；Access Token Signature使用RS256非對稱型加密，私鑰加密、公鑰解密，加密與解密分別由不同伺服端負責。

7. 實作Spring Security授權保護機制，設定基於角色(Role)的授權保護，讓受保護的API只有某種角色才能夠訪問。

8. 針對CORS的處理，讓Web瀏覽器能夠將跨來源的資料使用於JS，實現前後端分離。

9. gateway與各微服務實例之間，以Http協議實現交握註冊，並以定時任務建立心跳機制，確認彼此是否還活著。

10. gateway以亂數實現負載平衡(參考ApiGateway > jeff.apigateway.common.util.LoadBalanceUtil)。

11. 利用AOP將Logging邏輯從各業務邏輯程式碼中抽離出來，便於開發與維護。

12. 將Logging分級，針對可預期的例外按照嚴重程度區分，以不同等級的Log紀錄。

13. 使用RestTemplate實現各微服務間的溝通，以及與政府資料開放平台串接，下載DEMO用的館藏資料。

14. 使用Mockito實作單元測試，使用MockMvc實作整合測試(只有Book Server有整合測試)。

15. 使用MongoDB作為資料庫，並使用JPQL作為查詢語言，實現包含分頁查詢等等的基本CRUD功能。

<br><br>
## 環境(各Server都一樣)

開發與運行環境 : 至少JDK-17

資料庫 : Mongo DB-5.0.5

Spring Boot版本 : 2.7.0

<br>

#### [預設使用PORT]

ApiGateway : 8080

Authrntication-Service : 8081

Authorization-Service : 8083

Book-Service : 8082

Book-Service2 : 8084

MongoDB : 27017

<br><br>
## 事前建置與DEMO說明

#### [事前建置]

1. 安裝MongoDB(版本至少為5.0.5)，確保MongoDB運行於預設的PORT-27017。

2. 確保執行環境為JRE-17以上(各微服務Server都是)。

3. 啟動各Server，啟動順序不拘，系統會自己完成交握。

安裝MongoDB後，不用再做任何建置，程式啟動後會自行建立DB以及Collection，並且寫入DEMO用資料。

#### [DEMO說明]

***查看Swagger API文件的路徑:*** http://localhost:8080/api-docs

因為ApiGateway強依賴於Authentication-Service與Authorization-Service兩個微服務，所以必須等到那兩個微服務都啟動了，並且向ApiGateway完成交握註冊，那麼ApiGateway才會提供對外服務(包括Swagger API文件)。

***測試Load-Balance注意事項:***

確保Book-Service與Book-Service2都成功啟動並向gateway完成註冊，接著對Book相關API隨便連打請求，會看到回應的標頭有個「server-name」欄位的值隨機變化，有時是Book-Service，有時是Book-Service2。

_注意 : 若只啟動其中一個Book-Service，那Load-Balance機制就不會啟動_

<br><br>
## 系統架構示意圖

![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)

<br><br>
## 各微服務功能大致說明

### [ApiGateway] 

各微服務的監控(註冊與心跳機制)、JWT規格管理與派發、load balance轉發請求、訪問授權管制、Swagger API文件等等。

所有JWT所需的密鑰，都由Gateway啟動時統一生成並保管，然後等到所需的微服務向Gateway成功註冊後，Gateway會將所需的密鑰序列化後傳給該微服務使用。

***對外提供的API:***

1. 查看各微服務的狀態(GET) - 能夠查看所有ApiGate已知的微服務的服務狀態，例如實例A是否完成註冊並在服務中。

_詳情說明，洽Swagger API文件_

### [Authentication-Service(簡稱AuthN)]

帳密認證、生成Refresh Token與Access Token。

當AuthN啟動後，會先於MongoDB建置會員相關資料(三個分別代表不同權限的DEMO用資料)，接著會向Gateway註冊，並且要到Refresh Token的密鑰(加密與解密共用)，以及Access Token的私鑰(只用於加密)，以及其他例如EXP等等用以製作JWT所需的相關規格。

***對外提供的API:***

1. 帳密登入(POST) - 客戶端提供帳號密碼，由AuthN認證，若認證通過，就回傳RefreshToken以及AccessToken。

2. 刷新AccessToken(POST) - 客戶端提供RefreshToken，由AuthN進行解析，若解析正確，就回傳新的AccessToken。

_詳情說明，洽Swagger API文件_


### [Authorization-Service(簡稱AuthZ)]

專門解析Access Token。

當AuthZ啟動後，會向Gateway註冊，並且要到Access Token的公鑰，用以解析JWT。

***本服務端沒有對外開放的API***


### [Book-Service]

館藏相關的CRUD服務。

當Book啟動後，會先於MongoDB建置館藏相關DEMO資料(資料來源於 [政府資料開放平台](https://data.gov.tw/dataset/8727) )，接著會向Gateway註冊。

***對外提供的API:***

1. 查詢所有書籍(GET)

2. 以ID查詢某本書即(GET)

3. 以複合條件查詢所有書籍(GET)

4. 新增一本書(POST)

5. 取代一本書(PUT)

6. 部分更新一本書(PATCH)

7. 刪除一本書(DELETE)

_詳情說明，洽Swagger API文件_


<br><br>
## 可以改進的地方

1. _RestTemplate即將被棄用，可以改用WebClient取代_

RestTemplate雖為執行緒安全，但卻是同步阻塞的，沒有得到回應前不會執行下一行，然後該執行緒就這樣卡住，而Servlet容器會為每個請求都分配一個執行緒，直到該APP的執行緒到達上限後就爆掉，所以要設定超時、釋放資源；在不久後的將來RestTemplate會被SpringBoot官方推薦的WebClient取代，其為非同步不阻塞元件，還沒得到回應的話，該執行緒會先去執行別的任務(例如處理別的請求)，等到有回應了，看有沒有其他執行緒來接手繼續這個任務(此知識點尚未深入研究，以上為現階段粗淺理解)。

2. _分散式系統比較關鍵的跨DB跨伺服端的RollBack沒有做到_

如果要做，可以做在「館藏的借出與歸還」之功能上，但因為Spring Cloud似乎會幫忙完成此事，可以不用自己去撰寫RollBack相關邏輯，所以也許之後直接學Spring Cloud就好。

3. _各微服務的註冊與監控可以改用WebSocket_

目前各微服務實例向Gateway的註冊，都是採用基於Http協議的請求，並且以定時任務的方式發送心跳機制，告知自己還活著。各微服務每5秒會發送一次心跳，Gateway每10秒會掃描一次各微服務有沒有發來至少一次心跳，藉此監控微服務的狀態，但這麼做會有空窗期，假設某微服務突然關閉了，那麼Gateway要知道這件事情，得等到10秒以後觸發定時任務，再度掃描一次微服務狀態，才會知道哪些微服務已經關閉；若改用WebSocket實作微服務的交握註冊，就能夠更即時地監測到各微服務的狀態，只是也許更耗資源。


4. _基於Token的有狀態請求，可以改用redis實現Session共享_

一般使用JWT替代Session，是因為傳統基於Cookie的Session機制，並不能實現跨伺服端的Session共享；但現在有人使用redis實現跨伺服端的Session共享。

個人理解為把儲存Session的功能，再獨立拉出來一個伺服端做，一個Session可以對應多個SessionId(讓前端可以多裝置共享Session)，然後該Session Server的DB使用速度較快的redis。前端請求時，將不再使用Cookie了，而是將Cookie中的SessionId儲存在localStorage中，每次發送請求都夾帶於標頭上(類似於Token做法)，假設伺服端實例A接收到SessionId，就會拿SessionId去跟Session Server要Session資料，當然伺服端實例B也能拿SessionId去跟Session Server要Session資料，兩個伺服端實例取出的都是同一個Session，也就實現了跨伺服端的Session共享。

**使用JWT有以下缺點:**

* 空間以及長度問題

JWT為了記錄狀態，必須把很多資料包進Token(特別是AccessToken)，造成製作與解析上產生效能問題，以及JWT本身肥大的問題。

* 不能主動讓 Token 失效

JWT發放出去之後，不能透過ServerSide讓Token失效，必須等到exp時間過才會失去效用

有狀態的服務改回Session Based後，將不會有上述缺點，效能還比較好，且訪間流傳的關於Token Based的幾大優點「跨來源、CSRF安全、跨裝置」...其實Session Based也都可以做到，那並不算是Token Based獨有的優點。

