package com.handsome.club.paged_layout_manager.data

import com.handsome.club.paged_layout_manager.model.Cat

val exampleCats: List<Cat>
    get() = listOf(
        Cat("Jon"),
        Cat("Szarinka"),
        Cat("TonTon"),
        Cat("Beb"),
        Cat("Momuszko"),
        Cat("Gadzi"),
        Cat("Fołney"),
        Cat("Mołney"),
        Cat("Tojne"),
        Cat("Faszke"),
        Cat("Fiszke"),
        Cat("Moh"),
        Cat("Fih"),
    ).mapIndexed { index, cat -> cat.copy(name = "$index ${cat.name}") }