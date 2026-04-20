# IPTV Player (Android)

App Android en Kotlin para cargar una lista IPTV `.m3u`, mostrar canales y reproducirlos con ExoPlayer.

## Funcionalidad
- Pega la URL de una playlist M3U.
- Presiona **Cargar** para listar los canales.
- Toca un canal para reproducirlo.

## Requisitos
- Android Studio (Koala o superior)
- Android SDK API 35
- JDK 17 o 21 recomendado

## Generar APK (debug)
```bash
gradle :app:assembleDebug
```

APK esperado:
`app/build/outputs/apk/debug/app-debug.apk`
