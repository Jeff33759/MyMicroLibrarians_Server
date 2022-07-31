# MyMicroLibrarians_Server
分散式架構實作館藏系統


***＊＊＊＊＊＊＊＊＊＊＊＊＊專題大綱＊＊＊＊＊＊＊＊＊＊＊＊＊***

市面上虛擬主機的發展已臻成熟，隨著雲端的使用愈趨普遍，加上前後端分離的趨勢，分散式的系統架構逐漸受到歡迎。

有別於傳統的主從式架構，分散式架構除了可以幫助人們採用更加彈性的雲端部屬方案外，也有著更高的容錯性，缺點就是管理與開發要來得更加複雜。

本專題利用Spring Boot實作了分散式系統的模型，希望在學習使用Spring Cloud之前，能夠藉由實作本專題，更加了解分散式系統的運行機制。

***＊＊＊＊＊＊＊＊＊＊＊＊＊我做到了什麼＊＊＊＊＊＊＊＊＊＊＊＊＊***

1、RESTful Api，讓前後端分離的串接更加順利。
2、捨棄基於Cookie的Session機制，改用JSON Web Token(JWT)來記錄使用者的狀態，方便微服務之間共享使用者的公開資訊。
3、Refresh Token與Access Token的實作。
4、Refresh Token Signature使用HS256對稱型加密，加密與解密都由同個伺服端負責；Access Token Signature使用RS256非對稱型加密，私鑰加密、公鑰解密，加密與解密分別由不同伺服端負責。
5、使用AOP將Logging邏輯插入各業務邏輯中，紀錄所發生的例外，並將例外分級，以不同等級的Log紀錄。
6、使用AOP將RESTful Api的錯誤回應處理邏輯插入控制器，根據控制器拋出的例外不同，將其處理成不同的Http狀態碼，回應給客戶端。
7、使用RestTemplate實現各微服務間的溝通，以及與政府資料平台串接，下載DEMO用的館藏資料。
8、使用Mockito實作單元測試，使用MockMvc實作整合測試(只有Book Server有整合測試)。
9、

![image](https://raw.githubusercontent.com/Jeff33759/MyMicroLibrarians_Server/master/System_Architecture_Diagram.jpg
)
