# MyMicroLibrarians_Server
分散式架構實作館藏系統


***＊＊＊＊＊＊＊＊＊＊＊＊＊專題大綱＊＊＊＊＊＊＊＊＊＊＊＊＊***

市面上虛擬主機的發展已臻成熟，隨著雲端的使用愈趨普遍，加上前後端分離的趨勢，分散式的系統架構逐漸受到歡迎。

有別於傳統的主從式架構，分散式架構除了可以幫助人們採用更加彈性的雲端部屬方案外，也有著更高的容錯性，缺點就是管理與開發要來得更加複雜。

本專題利用Spring Boot實作了分散式系統的模型，希望在學習使用Spring Cloud之前，能夠藉由實作本專題，更加了解分散式系統的運行機制。


***＊＊＊＊＊＊＊＊＊＊＊＊＊我做到了什麼＊＊＊＊＊＊＊＊＊＊＊＊＊***

1、RESTful Api。

2、使用Swagger套件，撰寫API文件

3、捨棄基於Cookie的Session機制，改用JSON Web Token(JWT)來記錄使用者的狀態，方便微服務之間共享使用者的公開資訊。

4、客製化Spring Security的認證機制，實作自製的基於帳密的認證或是基於Token的認證邏輯。

5、Refresh Token與Access Token的實作。

6、Refresh Token Signature使用HS256對稱型加密，加密與解密都由同個伺服端負責；Access Token Signature使用RS256非對稱型加密，私鑰加密、公鑰解密，加密與解密分別由不同伺服端負責。

7、實作Spring Security授權保護機制，設定基於角色(Role)的授權保護，讓受保護的API只有某種角色才能夠訪問。

8、針對CORS的處理，讓Web瀏覽器能夠將跨來源的資料使用於JS，實現前後端分離。

9、gateway與各微服務實例之間，以Http協議實現交握，並以定時任務建立心跳機制，確認彼此是否還活著。

10、gateway以亂數實現負載平衡(參考ApiGateway > jeff.apigateway.common.util.LoadBalanceUtil)。

11、利用AOP將Logging邏輯從各業務邏輯程式碼中抽離出來，便於開發與維護。

12、將Logging分級，針對可預期的例外按照嚴重程度區分，以不同等級的Log紀錄。

13、使用RestTemplate實現各微服務間的溝通，以及與政府資料開放平台串接，下載DEMO用的館藏資料。

14、使用Mockito實作單元測試，使用MockMvc實作整合測試(只有Book Server有整合測試)。

15、使用MongoDB作為資料庫，並使用JPQL作為查詢語言，實現包含分頁查詢等等的基本CRUD功能。


***＊＊＊＊＊＊＊＊＊＊＊＊＊環境(各Server都一樣)＊＊＊＊＊＊＊＊＊＊＊＊＊***

開發與運行環境 : 至少JDK-17

資料庫 : Mongo DB-5.0.5

Spring Boot版本 : 2.7.0
  
--預設使用PORT--

ApiGateway : 8080

Authrntication-Service : 8081

Authorization-Service : 8083

Book-Service : 8082

Book-Service2 : 8084

MongoDB : 27017


***＊＊＊＊＊＊＊＊＊＊＊＊＊事前建置與DEMO說明＊＊＊＊＊＊＊＊＊＊＊＊＊***

[事前建置] : 

1、安裝MongoDB(版本至少為5.0.5)，確保MongoDB運行於預設的PORT-27017。

2、確保執行環境為JRE-17以上(各微服務Server都是)。

3、啟動ApiGateway、Authrntication-Service、Authorization-Service、Book-Service、Book-Service2，啟動順序不拘，系統會自己完成交握。

安裝MongoDB後，不用再做任何建置，程式啟動後會自行建立DB以及Collection，並且寫入DEMO用資料。

[查看Swagger API文件] : 

http://localhost:8080/api-docs

因為ApiGateway強依賴於Authentication-Service與Authorization-Service兩個微服務，所以必須等到那兩個微服務都啟動了，並且向ApiGateway完成交握註冊，那麼ApiGateway才會提供對外服務(包括Swagger API文件)。

[測試Load-Balance注意事項] : 

確保Book-Service與Book-Service2都成功啟動並向gateway完成註冊，接著對Book相關API隨便連打請求，會看到回應的標頭有個「server-name」欄位的值隨機變化，有時是Book-Service，有時是Book-Service2。

注意 : 若只啟動其中一個Book-Service，那Load-Balance機制就不會啟動。


***＊＊＊＊＊＊＊＊＊＊＊＊＊系統架構示意圖＊＊＊＊＊＊＊＊＊＊＊＊＊***

![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)



***＊＊＊＊＊＊＊＊＊＊＊＊＊各微服務功能大致說明＊＊＊＊＊＊＊＊＊＊＊＊＊***

[ApiGateway] : 

各微服務的監控(註冊與心跳機制)、JWT規格管理與派發、load balance轉發請求、訪問授權管制、Swagger API文件等等。

所有JWT所需的密鑰，都由Gateway啟動時統一生成並保管，然後等到所需的微服務向Gateway成功註冊後，Gateway會將所需的密鑰序列化後傳給該微服務使用。

對外提供的API : 

1、查看各微服務的狀態

詳情說明，洽Swagger API文件。

[Authentication-Service(簡稱AuthN)] : 

帳密認證、生成Refresh Token與Access Token。

當AuthN啟動後，會先於MongoDB建置會員相關資料(三個分別代表不同權限的DEMO用資料)，接著會向Gateway註冊，並且要到Refresh Token的密鑰(加密與解密共用)，以及Access Token的私鑰(只用於加密)，以及其他例如EXP等等用以製作JWT所需的相關規格。

對外提供的API:

1、帳密登入 - 客戶端提供帳號密碼，由AuthN認證，若認證通過，就回傳RefreshToken以及AccessToken。

2、刷新AccessToken - 客戶端提供RefreshToken，由AuthN進行解析，若解析正確，就回傳新的AccessToken。

詳情說明，洽Swagger API文件。


[Authorization-Service(簡稱AuthZ)] : 

專門解析Access Token。

當AuthZ啟動後，會向Gateway註冊，並且要到Access Token的公鑰，用以解析JWT。

本服務端沒有對外開放的API。


[Book-Service] : 

館藏相關的CRUD服務。

當Book啟動後，會先於MongoDB建置館藏相關DEMO資料(資料來源於政府資料開放平台)，接著會向Gateway註冊。

對外提供的API:

1、查詢所有書籍(GET)

2、以ID查詢某本書即(GET)

3、以複合條件查詢所有書籍(GET)

4、新增一本書(POST)

5、取代一本書(PUT)

6、部分更新一本書(PATCH)

7、刪除一本書(DELETE)

詳情說明，洽Swagger API文件。
