# О проекте
**SoundHub** - социальная сеть, ориентированная на *музыку* и *общение*.
Пользователи могут находить собеседников, потенциальных друзей по схожим музыкальным предпочтениям.
Социальная сеть SoundHub позволяет пользователям подписываться на новости музыкантов и следить за их публикациями и предлагает ему похожих исполнителей.
Предусмотрена интеграция музыкальных сервисов, таких как *Spotify*, *Яндекс.Музыка*, *Last.FM* для загрузки плейлистов и треков пользователя.

Для успешной сборки проекта необходимо включить некоторые константы в файл `local.properties` в корне проекта, а именно:
```properties
DISCOGS_KEY="Discogs key"
DISCOGS_SECRET="Discogs secret"

LAST_FM_API_KEY="last fm api key"
LAST_FM_SHARED_SECRET="last fm shared secret"
```
Также в файле `src/utils/constants/Constants.kt` указать адрес сервера приложения
```kotlin
object Constants {
    //...
    const val SOUNDHUB_API_HOSTNAME="real-server-host"
    const val SOUNDHUB_API = "http://$SOUNDHUB_API_HOSTNAME/api/v1/"
}
```

Все необходимые [Discogs](https://www.discogs.com/ru/applications/edit) и [Last.FM](https://www.last.fm/api/accounts?suspend=1) ключи можно получить при регистрации приложения API

[SoundHub API](https://github.com/LilYxa/soundhub-api)
# Участники проекта
- [Pr0gger1](https://github.com/Pr0gger1/)
- [LilYxa](https://github.com/LilYxa)
- [GaMe-PaDeR](https://github.com/GaMe-PaDeR)