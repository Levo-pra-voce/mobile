package com.levopravoce.mobile.common


fun formatCurrency(value: Double?): String {
    if (value == null) return "R$ 0,00"

    return "R$ ${"%.2f".format(value)}"
}