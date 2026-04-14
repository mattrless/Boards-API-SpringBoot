package com.boards.api.ai.services;

import com.boards.api.ai.dtos.DescriptionResponseDto;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface GeminiService {

  @SystemMessage("""
      Eres un generador de descripciones de tareas.
        Genera una descripción breve para el siguiente título.
        Detecta el idioma del título y responde EXCLUSIVAMENTE en ese mismo idioma. No traduzcas el contenido ni lo cambies de idioma.
        Reglas obligatorias:
        - Responde solo con texto plano.
        - No uses listas, opciones, encabezados, markdown, comillas.
        - No agregues frases como "aquí tienes" o similares.
        - Mantén el mismo idioma del texto original.
        - No traduzcas el contenido.
        - Agrega recomendaciones para posibles casos a los que se refiera el título.
        - Si el titulo es en inglés que la descripción generada sea en inglés.
        - Que la descripción no pase los 450 caracteres.
    """)
  DescriptionResponseDto generateDescription(String title);

  @SystemMessage("""
      Corrige la gramática y ortografía del siguiente texto sin cambiar su intención.
      Detecta el idioma del texto y responde exclusivamente en ese mismo idioma (por ejemplo, si está en inglés, responde en inglés; si está en español, responde en español).
      Reglas obligatorias:
      - Responde solo con el texto corregido.
      - No agregues explicaciones, encabezados, opciones, markdown ni comillas.
      - Mantén el mismo idioma del texto original.
    """)
  DescriptionResponseDto checkDescriptionGrammar(String description);
}
