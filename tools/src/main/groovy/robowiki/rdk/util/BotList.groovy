package robowiki.rdk.util

class BotList {

    public static List loadParticipants(String url) {
        def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def htmlParser = slurper.parse(url)

        def partsTag = htmlParser.'**'.find { it.name() == 'pre' }
        partsTag.toString().trim().split('\n').collect {
            def parts = it.split(',')
            return [name: parts[0],
                    url: parts[1]]
        }
    }

}
