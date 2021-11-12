# SOCIAL-CONNECT
project to connect with Facebook + Zalo
# kafka-connect-social
![image](https://user-images.githubusercontent.com/57492839/141406388-5996528a-32f4-4117-9d7d-e10ce12f1ebf.png)
  - Using webhook to connect to social, change events will be loaded into the Request Body part of the API call back placed on each webhook. 
From there, producer kafka will take those events and push them into topics
# social-adapter
![image](https://user-images.githubusercontent.com/57492839/141406557-8bf36156-1689-4221-8fa2-9b63b0124dcc.png)
  - Project builds API calls to Social's API to send messages to Social (visualize in Social screen)
# Reference

## Facebook API
  - https://developers.facebook.com/docs/messenger-platform/send-messages
## Zalo API
  - https://developers.zalo.me/docs/api/official-account-api/gui-tin-va-thong-bao-qua-oa/gui-thong-bao-van-ban-post-5072
  - https://developers.zalo.me/docs/api/official-account-api/gui-tin-va-thong-bao-qua-oa/gui-thong-bao-theo-mau-dinh-kem-anh-post-5068
  - https://developers.zalo.me/docs/api/official-account-api/gui-tin-va-thong-bao-qua-oa/gui-thong-bao-dinh-kem-file-post-5049
