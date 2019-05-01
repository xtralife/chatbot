#Generate jks key
keytool -genkey -keyalg RSA -alias $CHATBOT_HOST_ALIAS -keystore $CHATBOT_HTTPS_KEY_STORE_NAME.jks -storepass $CHATBOT_HTTPS_KEY_STORE_PASSWORD -validity 3600 -keysize 2048

#Transform jks key to pkcs12 key
keytool -importkeystore -srckeystore $CHATBOT_HTTPS_KEY_STORE_NAME.jks -destkeystore $CHATBOT_HTTPS_KEY_STORE_NAME.p12 -srcstoretype jks -deststoretype pkcs12

#Export pkcs12 key to pem file
openssl pkcs12 -in $CHATBOT_HTTPS_KEY_STORE_NAME.p12 -out $CHATBOT_HTTPS_KEY_STORE_NAME.pem -nokeys

#Set webHook for bot on Telegram
curl -F "url=https://$CHATBOT_HOST:8443/telegram/$CHATBOT_TELEGRAM_WEBHOOK_ENDPOINT" -F "certificate=@$CHATBOT_HTTPS_KEY_STORE_NAME.pem" https://api.telegram.org/bot$CHATBOT_TELEGRAM_TOKEN}/setWebhook
