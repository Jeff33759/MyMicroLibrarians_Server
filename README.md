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

開發與運行環境 : 至少要JDK17

資料庫 : Mongo DB 5.0.5

Spring Boot版本 : 2.7.0
  
預設使用PORT : 
ApiGateway : 8080
Authrntication-Service : 8081
Authorization-Service : 8083
Book-Service : 8082
Book-Service2 : 8084
MongoDB : 27017

***＊＊＊＊＊＊＊＊＊＊＊＊＊DEMO前的建置＊＊＊＊＊＊＊＊＊＊＊＊＊***

1、安裝MongoDB(版本至少為5.0.5)，確保MongoDB運行於預設的PORT-27017。

2、確保執行環境為JRE17以上(各微服務Server都是)。

3、啟動ApiGateway、Authrntication-Service、Authorization-Service、Book-Service、Book-Service2，順序不拘，會自己完成交握。

安裝MongoDB後，不用再做任何建置，程式啟動後會自行建立DB以及Collection，並且寫入DEMO用資料。


***＊＊＊＊＊＊＊＊＊＊＊＊＊系統架構示意圖＊＊＊＊＊＊＊＊＊＊＊＊＊***

![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)

***＊＊＊＊＊＊＊＊＊＊＊＊＊各微服務功能大致說明＊＊＊＊＊＊＊＊＊＊＊＊＊***

ApiGateway : 
