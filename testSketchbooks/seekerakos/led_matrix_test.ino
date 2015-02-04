// rows
int row1 = 10;
int row2 = 13;
int row3 = 9; 
int row4 = 6; 
int row5 = 2; 
int row6 = 8; 
int row7 = 3; 
//cols
int col1 = 12;
int col2 = 4; 
int col3 = 5; 
int col4 = 11;
int col5 = 7; 

void setup(){
  pinMode(row1,OUTPUT);
  pinMode(row2,OUTPUT);
  pinMode(row3,OUTPUT);
  pinMode(row4,OUTPUT);
  pinMode(row5,OUTPUT);
  pinMode(row6,OUTPUT);
  pinMode(row7,OUTPUT);
  pinMode(col1,OUTPUT);
  pinMode(col2,OUTPUT);
  pinMode(col3,OUTPUT);
  pinMode(col4,OUTPUT);
  pinMode(col5,OUTPUT);
  
}


void loop(){
  digitalWrite(row1,HIGH);
  digitalWrite(row2,HIGH);
  digitalWrite(row3,HIGH);
  digitalWrite(row4,HIGH);
  digitalWrite(row5,HIGH);
  digitalWrite(row6,HIGH);
  digitalWrite(row7,HIGH);
  digitalWrite(col1,LOW);
  digitalWrite(col2,LOW);
  digitalWrite(col3,LOW);
  digitalWrite(col4,LOW);
  digitalWrite(col5,LOW);
  
  delay(300);
   digitalWrite(row1,LOW);
  digitalWrite(row2,HIGH);
  digitalWrite(row3,HIGH);
  digitalWrite(row4,HIGH);
  digitalWrite(row5,HIGH);
  digitalWrite(row6,HIGH);
  digitalWrite(row7,LOW);
  digitalWrite(col1,HIGH);
  digitalWrite(col2,LOW);
  digitalWrite(col3,LOW);
  digitalWrite(col4,LOW);
  digitalWrite(col5,HIGH);
   delay(300);
   digitalWrite(row1,LOW);
  digitalWrite(row2,LOW);
  digitalWrite(row3,HIGH);
  digitalWrite(row4,HIGH);
  digitalWrite(row5,HIGH);
  digitalWrite(row6,LOW);
  digitalWrite(row7,LOW);
  digitalWrite(col1,HIGH);
  digitalWrite(col2,HIGH);
  digitalWrite(col3,LOW);
  digitalWrite(col4,HIGH);
  digitalWrite(col5,HIGH);
   delay(300);
}




 

 



