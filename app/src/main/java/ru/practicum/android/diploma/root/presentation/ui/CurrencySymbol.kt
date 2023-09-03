package ru.practicum.android.diploma.root.presentation.ui

enum class CurrencySymbol(val string: String, val symbol: String) {
    AZN("AZN", "₼"),
    BYR("BYR", "Br"),
    EUR("EUR", "€"),
    GEL("GEL", "₾"),
    KGS("KGS", "с"),
    KZT("KZT", "₸"),
    RUR("RUR", "₽"),
    UAH("UAH", "₴"),
    USD("USD", "$"),
    UZS("UZS", "сўм");

    companion object {
        fun getCurrencySymbol(currencyStr: String): String {
            for (currency in CurrencySymbol.values()) {
                if (currency.string == currencyStr) {
                    return currency.symbol
                }
            }
            return ""
        }
    }
}