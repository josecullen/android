/*
 *  Author: Jose Antonio Luceño Castilla
 *  Date  : Septempber 2013
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
#include <SoftwareSerial.h>

#define RxD 10
#define TxD 11
#define RST 5 // Encendido del Modulo
#define KEY 4

#define LED 13

#define LEDF1 2
#define LEDF2 3
#define LEDL2 6
#define LEDL1 7
#define LEDR1 8
#define LEDR2 9



SoftwareSerial BTSerial(RxD, TxD);
byte pinEstado = 0;
char command;
char cadena[255];
boolean isRead = false;
int i=0;
void setup()
{
  
  pinMode(LED, OUTPUT);
  pinMode(RST, OUTPUT);
  pinMode(KEY, OUTPUT);
  
  pinMode(LEDF1, OUTPUT);
  pinMode(LEDF2, OUTPUT);
  pinMode(LEDL1, OUTPUT);
  pinMode(LEDL2, OUTPUT);
  pinMode(LEDR1, OUTPUT);
  pinMode(LEDR2, OUTPUT);  
  
  // Estado inicial
  digitalWrite(LED, LOW);
  digitalWrite(RST, LOW);
  // Modo Comunicacion
  digitalWrite(KEY, LOW); 
   
  // Encendemos el modulo.
  digitalWrite(RST, HIGH);
  
  // Configuracion del puerto serie por software
  // para comunicar con el modulo HC-05
  BTSerial.begin(9600);
  BTSerial.flush();
  delay(500);
  
  // Configuramos el puerto serie de Arduino para Debug
  Serial.begin(9600);
  while(Serial.available())
    Serial.read();
  while(BTSerial.available())
    BTSerial.read();
  Serial.println("Ready");


}

void loop()
{
    if(BTSerial.available()){
       char dato = BTSerial.read();
       cadena[i++] = dato;
       
       if(dato == '_'){
          Serial.println(cadena); 
          splitString(cadena);
          clean();
       }
    }
   
}

void splitString(char* data){
   char* parameter;
  parameter = strtok(data, " ,"); 
  while(parameter != NULL){
     setDirection(parameter);
     parameter = strtok(NULL, " ,"); 
  }  
}

void setDirection(char* data){
   if((data[0] == 'x')){
      long x = strtol(data+1, NULL, 10);
      String xDir;
      if(x > -1) {
         digitalWrite(LEDF1, LOW);
         digitalWrite(LEDF2, LOW);
         xDir = "stop"; 
      }else if(x < -1){
          if(x < -3){
             digitalWrite(LEDF1, HIGH);
             digitalWrite(LEDF2, HIGH);
             xDir = "vel 2"; 
          } else{
             digitalWrite(LEDF1, HIGH);
             digitalWrite(LEDF2, LOW);
             xDir = "vel 1";             
          }
      }
      Serial.println(xDir);
   } 
   if((data[0] == 'y')){
      long y = strtol(data+1, NULL, 10);
      String xDir;
      if(y > -1 && y < 1) {
         digitalWrite(LEDL1, LOW);
         digitalWrite(LEDL2, LOW);
         digitalWrite(LEDR1, LOW);
         digitalWrite(LEDR2, LOW);
         xDir = "derecho"; 
      }else if(y < -1){
        if(y < -3) {
           digitalWrite(LEDL1, HIGH);
           digitalWrite(LEDL2, HIGH);
           digitalWrite(LEDR1, LOW);
           digitalWrite(LEDR2, LOW);
           xDir = "izq 2"; 
        }  else{
          digitalWrite(LEDL1, HIGH);
          digitalWrite(LEDL2, LOW);
          digitalWrite(LEDR1, LOW);
          digitalWrite(LEDR2, LOW);
          xDir = "izq 1"; 
        }             
      }else if(y > 1){
        if(y > 3){
           digitalWrite(LEDL1, LOW);
           digitalWrite(LEDL2, LOW);
           digitalWrite(LEDR1, HIGH);
           digitalWrite(LEDR2, HIGH);
           xDir = "der 2";
        } else{
          digitalWrite(LEDL1, LOW);
          digitalWrite(LEDL2, LOW);
          digitalWrite(LEDR1, HIGH);
          digitalWrite(LEDR2, LOW);
           xDir = "der 1"; 
        }
            
      }
      Serial.println(xDir);
   } 
 
}


void clean(){
   for(int cl = 0; cl <= i; cl++){
      cadena[cl] = 0;
   } 
   i = 0;
}

