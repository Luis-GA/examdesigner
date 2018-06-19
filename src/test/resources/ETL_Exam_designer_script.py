import re
import json

def ETL():
    # Parametros de preguntas
    CantidadPreguntas = 1
    CantidadRespuestas = 4
    NombreTema = 'CONCEPTOS DE TEORÍA DE LA INFORMACIÓN'
    idTema = 1

    # Expresiones regulares
    Enunciado = r"([0-9])+(.)(\s)"
    Respuestas = r"([abcd](.)(\s))"
    RespuestaCorrecta = " (CORRECTA)"

    # Diccionario
    inicioRespuestas = {1: 'a', 2: 'b', 3: 'c', 4: 'd', 5: 'e', 6: 'f'}

    # Abrir fichero
    with open("fichero", "r", encoding="UTF-8") as f:
        content = f.readlines()

    # Contadores
    Contador_Preguntas = 0
    Contador_respuestas = 0
    id = 1

    # Variables de datos
    test = {}
    TESTS = []
    # Lectura de fichero y análisis de preguntas y respuestas
    for linea in content:

        if (re.match(Enunciado, linea) is not None):

            #test["idPregunta"] = id
            test["type"]="TEST"
            test["bodyObjects"]= []
            test["weight"]= 0
            test["duration"]= 0
            test["title"] = re.sub("\n","",re.sub(Enunciado, "", linea))
            test['correctChoices']=[]
            test['choices'] = {}
            id = id + 1
            Contador_Preguntas = Contador_Preguntas + 1

        elif (re.match(Respuestas, linea) is not None):
            aux = str(linea)
            linea = linea.replace(RespuestaCorrecta, "")
            if not (linea.__eq__(aux)):
                test['correctChoices'].append(re.sub("\n","",linea[3:]))

            clave = linea[0]
            linea = re.sub("\n","",linea[3:])
            test['choices'][clave] = {
                  "title": linea,
                  "bodyObjects": []
                }
            Contador_respuestas = Contador_respuestas + 1
            if (clave == inicioRespuestas[CantidadRespuestas]):
                TESTS.append(dict(test))
    f.close()
    # Comprobación de que no ha fallado la detección de expresiones regulares
    if not (CantidadPreguntas == Contador_Preguntas and Contador_respuestas / CantidadRespuestas == float(
            CantidadPreguntas)):
        print("ERROR")
        raise Exception
    # Preparado de datos y guardado en fichero
    TESTS = {'topic': NombreTema, 'idTopic': idTema, 'questions': TESTS}

    data = json.dumps(TESTS, indent=4, sort_keys=True)

    with open(NombreTema + ".json", 'w', encoding="UTF-8") as outfile:
        outfile.write(data)
