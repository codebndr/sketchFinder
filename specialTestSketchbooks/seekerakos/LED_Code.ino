int ledPin = 13; //dilwnoume to LED stin thura 13

void setup() // einai mia sunartisi pou kalleite kathe fora pou grafoyme ena programma gia arduino
{
  pinMode(ledPin, OUTPUT); //edw dilwnoume oti to ledPin einai pin eksodou
}

void loop()//kai auti einai i deuteri sunartisi pou xrisimopoioume pou dilwnoume ti tha epanalamvanetai mesa sto arduino
{
  digitalWrite(ledPin, HIGH); // me auti tin entoli anavoume to LED
  delay(2000); // uparxei 2 sec kathiserisi mexri na diavastei i epomeni entoli
  digitalWrite(ledPin, LOW); // kai auti i entoli svinei to LED
  delay(1000);// kai twra ena sec kathiserisi mexri na ksanaepistrepsei stin prwti entoli
}
