# О проекте

**SoundHub** - социальная сеть, ориентированная на *музыку* и *общение*.
Пользователи могут находить собеседников, потенциальных друзей по схожим музыкальным предпочтениям.
Социальная сеть SoundHub позволяет пользователям подписываться на новости музыкантов и следить за их
публикациями и предлагает ему похожих исполнителей.
Предусмотрена интеграция музыкальных сервисов, таких как *VK*, *Last.FM* для загрузки плейлистов и
треков пользователя.

Для успешной сборки проекта необходимо включить некоторые константы в файл `local.properties` в
корне проекта, а именно:

```properties
DISCOGS_KEY="Discogs key"
DISCOGS_SECRET="Discogs secret"
SOUNDHUB_API_HOSTNAME="your-host-address" // example.com or 192.168.xxx.xxx for localhost
LAST_FM_API_KEY="last fm api key"
LAST_FM_SHARED_SECRET="last fm shared secret"
```

Также необходимо создать в корне проекта файл `gradle.properties` и установить свойство:

```properties
android.useAndroidX=true
```

Все необходимые [Discogs](https://www.discogs.com/ru/applications/edit)
и [Last.FM](https://www.last.fm/api/accounts?suspend=1) ключи можно получить при регистрации
приложения API

[Актуальная версия API](https://github.com/Pr0gger1/soundhub-api)

[SoundHub API (оригинальный репозиторий,разработка прекращена)](https://github.com/LilYxa/soundhub-api)

# Участники проекта

- [Pr0gger1](https://github.com/Pr0gger1/)
- [LilYxa](https://github.com/LilYxa)
- [GaMe-PaDeR](https://github.com/GaMe-PaDeR)
