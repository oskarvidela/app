# Ruta San Eugenio · App Android (prototipo)

App Android de la Ruta Patrimonial Maestranza San Eugenio (EFE Cultura).
Funciona sin internet: el mapa, los pictogramas y los textos van dentro de la app.
La tipografía Archivo se descarga solo cuando hay conexión; sin conexión se usa la del teléfono.

## Obtener el .apk sin instalar nada (GitHub)

1. Crea un repositorio nuevo en github.com y sube todo el contenido de esta carpeta
   (incluida la carpeta oculta `.github`).
2. Entra a la pestaña **Actions**. El proceso "Generar APK" parte solo y tarda unos 5 minutos.
3. Al terminar, abre la ejecución y descarga **RutaSanEugenio-apk** (viene en un .zip).
4. Pasa el `app-debug.apk` al teléfono Android, ábrelo y acepta "instalar apps de origen desconocido".

## Con Android Studio

Abre esta carpeta con Android Studio y presiona Run (▶). Para publicar en Google Play:
Build → Generate Signed Bundle / APK (cuenta de Google Play Console, pago único de US$25).

## Enlaces para QR y NFC

Las etiquetas NFC o códigos QR pueden abrir la app directo en una parada:

    efecultura://mse/p1   …   efecultura://mse/p9

## Cambiar contenido

- Textos de las paradas: `app/src/main/assets/www/index.html`, lista `STOPS`.
- Imágenes: `app/src/main/assets/www/img/`.
