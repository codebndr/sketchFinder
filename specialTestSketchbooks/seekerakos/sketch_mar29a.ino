int piano1= 13;


void setup(){

  pinMode(piano1,INPUT);
  Serial.begin(9600);
}

void loop(){
  if(digitalRead(piano1)== HIGH){
    Serial.write("High");  
  }else{
   Serial.write("LOW");
  }
  delay(100);

}
