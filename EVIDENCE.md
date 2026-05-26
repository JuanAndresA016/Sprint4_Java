1.	What model did you use? How much RAM does it use? (check Task Manager / Activity Monitor)

    El modelo que utlizamos fue qwen2.5:3b y utilizaba 2.1GB de Ram


2.	What happens if you send a follow-up message without the conversation history? Test it and describe the result.

    El modelo contesta como si la conversación nunca se hubiera iniciado, es decir no recuerda nada de las conversaciones anteriores


3.	Try the same question with temperature:0.0 and temperature:0.9 in the request body. What changes? (hint: add it to the JSON payload)

    Cuando se usa una temperatura de 0.0 la respuesta siempre es la misma mientras que con 0.9 suelen varias
