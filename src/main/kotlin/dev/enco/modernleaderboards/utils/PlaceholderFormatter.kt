package dev.enco.modernleaderboards.utils

object PlaceholderFormatter {

    fun format(text: String, keys: Array<String>?, values: Array<String>?): String {
        if (keys == null || values == null) return text

        val keysLen = keys.size
        val valuesLen = values.size

        if (keysLen == 0 || valuesLen == 0) return text
        require(keysLen == valuesLen) { "Values and keys must have the same lengths" }

        return if (keysLen < 5) {
            formatShort(text, keys, values)
        } else {
            formatBig(text, keys, values)
        }
    }

    private fun formatBig(text: String, keys: Array<String>, values: Array<String>): String {
        val first = text.indexOf('{')
        if (first == -1) return text

        val sb = StringBuilder(text.length + 64)
        var cur = 0; var pos = first

        while (pos != -1) {
            sb.append(text, cur, pos)

            val end = text.indexOf('}', pos + 1)
            if (end == -1) break

            val len = end - pos + 1
            val firstChar = text[pos + 1]

            var replaced = false

            for (i in keys.indices) {
                val k = keys[i]

                if (k[1] != firstChar) continue

                if (k.length == len && text.regionMatches(pos, k, 0, len)) {
                    sb.append(values[i])
                    cur = end + 1
                    pos = text.indexOf('{', cur)
                    replaced = true
                    break
                }
            }

            if (replaced) continue

            sb.append(text, pos, end + 1)
            cur = end + 1
            pos = text.indexOf('{', cur)
        }

        if (cur < text.length) {
            sb.append(text, cur, text.length)
        }

        return sb.toString()
    }

    private fun formatShort(text: String, keys: Array<String>, values: Array<String>): String {
        val result = StringBuilder(text)

        for (i in keys.indices) {
            val key = keys[i]
            val value = values[i]

            var start = 0

            while (true) {
                start = result.indexOf(key, start)
                if (start == -1) break

                result.replace(start, start + key.length, value)
                start += value.length
            }
        }

        return result.toString()
    }
}