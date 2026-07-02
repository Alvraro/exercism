def is_vowel(letter):
    lower = letter.lower()
    return lower == "a" or lower == "e" or lower == "i" or lower == "o" or lower == "u"

def translate(text):
    words = text.split(" ")
    translated_words = []
    for word in words:
        translated_words.append(translate_word(word))
    return " ".join(translated_words)

def translate_word(word):
    first2 = word[0:2]
    # Rule 1
    if is_vowel(word[0]) or first2 == "xr" or first2 == "yt":
        word = word + "ay"

    else:
        num_consonants = 0
        while num_consonants < len(word) and not(is_vowel(word[num_consonants])):            
            num_consonants += 1
            if word[num_consonants-1] == "y":
                break
        
        # Rule 3
        if len(word) >= 2 and num_consonants >= 1 and word[num_consonants-1:num_consonants+1] == "qu":
            word = word[num_consonants+1:] + word[0:num_consonants+1] + "ay"

        # Rule 4
        elif num_consonants >= 2 and word[num_consonants-1] == "y":
            word = word[num_consonants-1:] + word[0:num_consonants-1] + "ay"

        # Rule 2
        elif num_consonants > 0:
            word = word[num_consonants:] + word[0:num_consonants] + "ay"

    return word