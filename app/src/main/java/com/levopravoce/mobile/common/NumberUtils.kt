package com.levopravoce.mobile.common


fun formatCurrency(value: Double): String {
    return "R$ ${"%.2f".format(value)}"
}