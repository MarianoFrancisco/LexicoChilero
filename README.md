Compilador CHILERO (primera fase)
=================================

Al iniciar la tarea, su repositorio debera contener los siguientes archivos y directorios importantes:

* Makefile
* README.md
* lexer-chilero
* lexer-chilero/src/main/java/gt/edu/usac/compiler
* lexer-chilero/src/main/jflex
* cooltests

`Makefile` contiene objetivos para compilar y ejecutar su programa. NO MODIFICAR.

`README.md` contiene esta información. 

`lexer-chilero/src/main/jflex/simpletokenizer.lex` es un archivo esqueleto para la especificación del analizador léxico. Debera completarlo con expresiones, patrones y acciones.

Dentro de `test.cl` encontrará tres programas de prueba:

1. `test.cl` en el cual puede probar análisis léxico y recuperación de errores. Este programa NO es correcto y le puede servir para verificar que su analizador léxico detecte correctamente los errores. Así mismo debera modificarlo para que sea correcto.
2. `factloop.cl` es una calculadora de factorial previamente elaborada en el laboratorio.
3. `stack.cl` es una solución a la máquina de pila previamente elaborada en el laboratorio.

`lexer-chilero/src/main/java/gt/edu/usac/TokenConstants.java` contiene definiciones de constantes que utilizan casi todas las partes del compilador. NO MODIFICAR.

`lexer-chilero/src/main/java/gt/edu/usac/Table.java` y `lexer-chilero/src/main/java/gt/edu/usac/Symbol.java` contienen tablas de cadenas y símbolos útiles en las siguientes fases del compilador NO MODIFICAR.

`lexer-chilero/src/main/java/gt/edu/usac/Utilities.java` contiene varias funciones de soporte utilizadas por el controlador lexer principal (Lexer.java). NO MODIFICAR.

`mycoolc` es un script de shell que une las fases del compilador que utiliza pipes de Unix en lugar de vincular estáticamente el código.  Si bien es ineficiente, esta arquitectura facilita la combinación y combinación los componentes que escribe con los del compilador del curso. NO MODIFICAR.

Tareas principales
------------------

1. Implemente el analizador léxico para COOL y su dialecto CHILERO en el archivo `lexer-chilero/src/main/jflex/simpletokenizer.lex`.
2. En la última sección de este archivo debera redactar una explicación clara de su solución en lenguaje académico (formal, en tercera persona, directo). Debera explicar las decisiones de diseño, como implementó el soporte al dialecto, si necesitó estados intermedios en JFlex, explicar por qué su código es correcto y que modificaciones se realizaron sobre `test.cl` para que fuera un programa correcto.  Asi mismo debe comentar el código que considere conveniente en JFlex.
3. En el archivo `lexer-chilero/src/main/java/gt/edu/usac/Lexer.java` encontrará una implementación de un método principal que invoca a su lexer e imprime los tokens que se detectan. Esta salida es obligatoria para las siguientes fases del proyecto. Debera escribir su propia versión utilizando PicoCLI tal y como se vio en el laboratorio. Esta versión también debera soportar análisis individual de Tokens.
4. El día de la entrega/calificación su instructor le indicará algunas modificaciones con pruebas adicionales (programas en español, ejecuciones de lexer en línea de comandos). El cual usted debera aceptar para comprobar el análisis de su solución.

Note que el autograder proporcionado con este repositorio incluye únicamente pruebas sobre stack y factorial

Instrucciones de uso del makefile
---------------------------------

El archivo Makefile incluido en su repositorio incluye algunas tareas útiles que pueden ser invocadas directamente en la línea de comandos.

Para compilar su programa ejecute

> make compile

Para generar un script `lexer` que ejecute su analizador léxico, ejecute

> make lexer

Para limpiar todo el proyecto ejecute:

> make clean

Así mismo existen dos fases adicionales que ejecutan en secuencia varios pasos para una prueba completa de su solución.

La primera es `dofactorial` la cual limpia su proyecto, ejecuta la compilación, genera el lexer y ejecuta una prueba del programa `factloop.cl`.

La segunda es `dostack` la cual limpia su proyecto, ejecuta la compilación, genera el lexer y ejecuta una prueba del programa `stack.cl`.

Si cree que su analizador léxico es correcto, puede ejecutar `mycoolc` el cual une SU analizador léxico con, los analizadores sintácticos, semánticos, generador de código y optimizador de cool. 

Si su analizador léxico se comporta de una manera inesperada, puede obtener errores en cualquier lugar, es decir, durante análisis sintáctico, durante el análisis semántico, durante la generación de código o solo cuando ejecuta el código producido en spim. 

¡Éxitos!

Redacción para primera fase
---------------------------  
`Decisiones de diseño`  
Establecido el objetivo de crear un analizador léxico que sustituya al que tiene el lenguaje Cool con la posibilidad de tener soporte al dialecto CHILERO se define:  
1. Utilizar el archivo simpletokenizer.lex para la creacion del analizador lexico.  
2. Establecer definiciones entendibles para uso posterior en reglas.  
3. Definir estados intermedios para movilidad entre distintas acciones.  
4. Utilizar StringBuffer para creacion de cadenas.
5. Permitir visibilidad publica de la clase.  
6. Colocar primeramente todos los estados YYINITIAL.  
7. Establecer prioridad de palabras reservadas ante identificadores.

`Implementacion al soporte al dialecto`  

Las definiciones son importantes para establecer el soporte al dialecto, donde las palabras reservadas tienen la posibilidad de venir escritas en español-GT como en ingles, siendo case insensitive a excepcion de las palabras reservadas true y false donde la primera letra es minuscula y las demas nuevamente case insensitive, de la misma manera si fuera en español-GT, teniendo como ejemplo if = [iI][fF]|[sS][iI], donde se acepta la validacion de if, si fuera iF, sI, Si, If, entre otras variaciones, se devuelve un if independientemente de la forma que este venga.
   
`Utilización de estados intermedios`  

 Se requiere identificar cuando hay errores en un string o en comentario multilínea, para ello se definen los estados intermedios STRING y MULTI_COMENTARIO, así en la instrucción eofval se le brinda que error puede suceder si al finalizar aún permencen en estos estados y no en YYINITIAL.  
   
`Definicion general` 

La solucion al analizador lexico se da a grandes rasgos con las definiciones que permiten obtener un token independientemente si fuera en ingles o es-GT, y las demás que son importantes, operadores, comentarios, identificadores y simbolos, como tambien las declaraciones con la creacion de metodos de reseteo y variables para uso en las reglas.  

Se establecen las definiciones y declaraciones, en el sentido que se usarán para las reglas parte crucial para establecer el analizador, sin esto no se puede saber cuando se obtiene un token cuando se posee errores, por lo tanto se crea primero los YYINITIAL donde se establecen palabras reservadas, operadores, identificadores,digitos, simbolos, seguido de los STRING donde van las cadenas y se realiza el llamado a las variables y metodos de declaraciones para saber que ruta tomar, finalizando con los MULTI_COMENTARIO, donde al detectar un token correcto se retorna un simbolo según corresponda, y en los casos de cadena a StringTable, enteros a IntTable y los identificadores de tipo y de objeto a IdTable se agrega su información adicional con AbstractTable. Por último la definicion de un caracter no definido para la formación de un token se identifica con error.

`¿Por qué el código es correcto?`

El código es correcto estableciendo todos los token para la funcionalidad del analizador léxico que reemplaza al de Cool, así mismo identifica los errores que puedan surgir por mala escritura, como falta de cierre en cadenas o comentarios, exceso de caracteres en cadenas, token inexistentes, entre otros posibles errores, cumpliendo también con su funcionalidad de entender el dialecto CHILERO y ejecutar correctamente los procesos de Cool para llegar a crear el codigo correspondiente.

`Modificaciones sobre test.cl`  
Se realiza el análisis del léxico y pasa a mycoolc, necesita comprobarse que se den los errores y que lleguen correctamente al pasar los datos por mycoolc.

Primera ejecucion, lexer detecta seis errores:  
#56 ERROR "'"  
#56 ERROR "'"  
#62 ERROR "["  
#62 ERROR "]"  
#86 ERROR ">"  
#97 ERROR "EOF in multi-line comment".  

Y al pasar por mycoolc se obtiene error línea 56 siendo o cerca de ', línea 62 siendo o cerca de [ y línea 86 siendo o cerca de >.

1. En la línea 56 donde está caracter . se encuentra encerado en '', caracter invalido por que variable char no existe directamente en Cool, se sustituye por comillas "".
2. En la línea 62 donde se encuentran los caracteres [], se intenta utilizar un arreglo, lo cual no existe directamente en cool, se sustituye por parentesis (). 
3. En la línea 86 se encuentra el caracter >, en el lenguaje Cool únicamente existe el simbolo de <, se mantiene la lógica y se hace intercambio de las variables que afectan, countdown y 0 se intercambian, y se modifica el caracter > por <.  
4. En la línea 98 se encuentra el error EOF in multi-line comment, indica que no hubo cierre del multicomentario, solo inicializacion, se inicia en la línea 93 y después tiene cosas necesarias para cierre de código, entonces se agrega finalización de multi comentario agregando *) al final de la línea 93.

Segunda ejecucion, lexer no detecta ningun error, no hay errores lexicos. Y al pasar por mycoolc se obtiene error línea 92 siendo o cerca de POOL y línea 96 siendo o cerca de }.

1. En la línea 92 se encuentra POOL, indica el cierre de un while, dentro del while se inicia un bloque con {, antes de POOL hace falta su cierre, se  agrega el caracter } antes de POOL, el error en la línea 96 detectaba que el caracter } se relacionaba con algo incorrecto, sobrando un }, al solucionar el error de la línea 92 se soluciona el de la línea 96.

Tercera ejecucion, ningún error en lexer ni en mycoolc generando resultado de ejecución correcta.# LexicoChilero
