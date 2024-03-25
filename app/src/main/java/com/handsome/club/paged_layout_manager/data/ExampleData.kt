package com.handsome.club.paged_layout_manager.data

import com.handsome.club.paged_layout_manager.model.Cat

val exampleCats: List<Cat>
    get() = listOf(
        "Coco",
        "Don Juan",
        "Pebbles",
        "Yoshi",
        "Smokey",
        "Bolt",
        "Sancho",
        "Jeckyll",
        "Darth Vader",
        "Frisco",
        "Bubba",
        "Little Bub",
        "Hulk",
        "T-Bone",
        "Ambassador",
        "Whiskey",
        "Fuzzy",
        "Pepe",
        "Brutus",
        "Alfonso",
        "Govenor",
        "Boss",
    )
        .let{ it+it }
        .map(::Cat)
        .mapIndexed { index, cat -> cat.copy(name = "$index ${cat.name}") }