int enable = 2;
int inp1 = 3;
int inp2 = 4;
int enablesw=5;
int enableon=6;
boolean swtch = false;
boolean onoff = false;

void setup()
{
    pinMode(enable,OUTPUT);
    pinMode(inp1,OUTPUT);
    pinMode(inp2,OUTPUT);
    pinMode(enablesw,INPUT);
    pinMode(swtch,INPUT);
    digitalWrite(enable,HIGH);
}

void loop()
{
	if (enablesw==HIGH && swtch==false)
	{
		
		swtch = !swtch;
		digitalWrite(inp1,HIGH);
		digitalWrite(inp1,LOW);
	}
	else
	{
		swtch=!swtch;
		digitalWrite(inp2,HIGH);
		digitalWrite(inp1,LOW);
	}
	
	if (enableon==HIGH && onoff== false)
	{
		onoff = !onoff;
		digitalWrite(enable,LOW);
	}
	else
	{
		onoff= !onoff;
		digitalWrite(enable,HIGH);
	}
}
