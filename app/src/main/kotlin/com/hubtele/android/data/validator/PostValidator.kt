package com.hubtele.android.data.validator

class PostValidator {
    companion object {
        fun isValid(text: String): ValidateResult {
            if (text == null) {
                return ValidateResult(false, "コメントを入力してください")
            } else if (text.length > 500) {
                return ValidateResult(false, "コメントは500文字以内で入力してください")
            }
            return ValidateResult(true);
        }
    }
}