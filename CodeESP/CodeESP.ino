#include <ESP8266WiFi.h>
#include "FirebaseESP8266.h"

#define FIREBASE_HOST "Điền Vào .com"
#define FIREBASE_AUTH "ĐIền vào"
#define WIFI_SSID "my_ssid"
#define WIFI_PASSWORD "my_password"

#define SHOWPin D0
#define DenChinhPin D1
#define DenHocPin D2
#define DenNguPin D3
#define Quat1Pin D4
#define Quat2Pin D5
#define CoiPin D6
#define CuaPin D7

#define KHIGAPin A0
#include "DHT.h"
#define DHTPIN D8
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);


FirebaseData firebaseData;
String path = "/";
FirebaseJson json;
long long last = 0, timerCua = 0;
int a=1,b=1;

String Cua = "OFF", DenChinh = "OFF", DenHoc = "OFF", DenNgu = "OFF", Quat1 = "OFF", Quat2 = "OFF";
String KhiGa = "45", NhietDo = "28", DoAm = "30";

void ReadDataToFirebase();
void WriteDataToFirebase(String NhietDo, String DoAm, String KhiGa);
void HandleRelay();

void setup()
{
  pinMode(SHOWPin, OUTPUT);
  pinMode(DenChinhPin, OUTPUT);
  pinMode(DenNguPin, OUTPUT);
  pinMode(DenHocPin, OUTPUT);
  pinMode(Quat1Pin, OUTPUT);
  pinMode(Quat2Pin, OUTPUT);
  pinMode(CoiPin, OUTPUT);
  pinMode(CuaPin, OUTPUT);
  Serial.begin(9600);

  digitalWrite(SHOWPin, 0);
  digitalWrite(DenChinhPin, 0);
  digitalWrite(DenNguPin, 0);
  digitalWrite(DenHocPin, 0);
  digitalWrite(Quat1Pin, 0);
  digitalWrite(Quat2Pin, 0);
  digitalWrite(CoiPin, 0);
  digitalWrite(CuaPin, 0);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ") ;
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  if (!Firebase.beginStream(firebaseData, path))
  {
    Serial.println("REASON:+ " + firebaseData.errorReason());
    Serial.println();
  }
  
  last = millis();
  dht.begin();

}

void loop()
{
  int d = dht.readHumidity();
  int t = dht.readTemperature();
  int k = analogRead(KHIGAPin) / 10.0 - 20;
  if (k > 65) digitalWrite(CoiPin, 1);
  else digitalWrite(CoiPin, 0);
  NhietDo = String(t);
  DoAm = String(d);
  KhiGa = String(k);

  digitalWrite(SHOWPin, !digitalRead(SHOWPin));
  WriteDataToFirebase( NhietDo , DoAm,  KhiGa);
  ReadDataToFirebase();
  HandleRelay();
  Serial.println("Cua " + Cua + "DenChinh " + DenChinh + "DenHoc " + DenHoc + "DenNgu " + DenNgu + "Quat1 " + Quat1 + "Quat2 " + Quat2);
  Serial.println("NhietDO " + NhietDo + " DoAm " + DoAm + " KhiGa " + KhiGa);

}
void HandleRelay()
{
  if (Cua == "ON")
  {
    if(a==0)
    {
      a=1;
      timerCua=millis();
    }
    if(millis()-timerCua > 8000)
    {
      digitalWrite(CuaPin, 0);
    }
    else
    {
       digitalWrite(CuaPin, 1);
    }
  }
  else
  {
    digitalWrite(CuaPin, 0);
    a=0;
  }

  if (DenChinh == "ON")
  {
    digitalWrite(DenChinhPin, 1);
  }
  else
  {
    digitalWrite(DenChinhPin, 0);
  }
  if (DenHoc == "ON")
  {
    digitalWrite(DenHocPin, 1);
  }
  else
  {
    digitalWrite(DenHocPin, 0);
  }
  if (DenNgu == "ON")
  {
    digitalWrite(DenNguPin, 1);
  }
  else
  {
    digitalWrite(DenNguPin, 0);
  }
  if (Quat1 == "ON")
  {
    digitalWrite(Quat1Pin, 1);
  }
  else
  {
    digitalWrite(Quat1Pin, 0);
  }
  if (Quat2 == "ON")
  {
    digitalWrite(Quat2Pin, 1);
  }
  else
  {
    digitalWrite(Quat2Pin, 0);
  }
}
void ReadDataToFirebase()
{
  if (Firebase.getString(firebaseData, path + "/Cua"))
  {
    Cua = firebaseData.stringData();
  }
  if (Firebase.getString(firebaseData, path + "/DenChinh"))
  {
    DenChinh = firebaseData.stringData();
  }
  if (Firebase.getString(firebaseData, path + "/DenHoc"))
  {
    DenHoc = firebaseData.stringData();
  }
  if (Firebase.getString(firebaseData, path + "/DenNgu"))
  {
    DenNgu = firebaseData.stringData();
  }
  if (Firebase.getString(firebaseData, path + "/Quat1"))
  {
    Quat1 = firebaseData.stringData();
  }
  if (Firebase.getString(firebaseData, path + "/Quat2"))
  {
    Quat2 = firebaseData.stringData();
  }
}
void WriteDataToFirebase(String nhietdo, String doam, String khiga)
{
  if (millis() - last > 500)
  {
    Firebase.setString(firebaseData, path + "/KhiGa", khiga);
    Firebase.setString(firebaseData, path + "/NhietDo", nhietdo);
    Firebase.setString(firebaseData, path + "/DoAm", doam);
    
    last = millis();
  }
}
