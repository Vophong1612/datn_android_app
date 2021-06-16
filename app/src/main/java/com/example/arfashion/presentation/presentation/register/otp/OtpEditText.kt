//import android.app.Activity
//import android.content.Context
//import android.text.Editable
//
//import android.text.TextWatcher
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import com.example.arfashion.R
//
//class OtpEditText(private val view: EditText, activity: Activity) : TextWatcher {
//
//    private val first = activity.findViewById<EditText>(R.id.first)
//    private val second = activity.findViewById<EditText>(R.id.second)
//    private val third = activity.findViewById<EditText>(R.id.third)
//    private val fourth = activity.findViewById<EditText>(R.id.fourth)
//    private val fifth = activity.findViewById<EditText>(R.id.fifth)
//    private val sixth = activity.findViewById<EditText>(R.id.sixth)
//
//    override fun afterTextChanged(editable: Editable) {
//        val text = editable.toString()
//
//        when (view.id) {
//            R.id.first -> {
//                if (text.length == 1) second.requestFocus()
//            }
//            R.id.second -> {
//                if (text.length == 1) third.requestFocus() else if (text.isEmpty()) {
//                    first.requestFocus()
//                }
//            }
//            R.id.third ->{
//                if (text.length == 1) fourth.requestFocus() else if (text.isEmpty()) second.requestFocus()
//            }
//            R.id.fourth -> {
//                if (text.length == 1) fifth.requestFocus() else if (text.isEmpty()) third.requestFocus()
//            }
//            R.id.fifth -> {
//                if (text.length == 1) sixth.requestFocus() else if (text.isEmpty()) fourth.requestFocus()
//            }
//            R.id.sixth -> {
//                if (text.isEmpty()) fifth.requestFocus()
//            }
//        }
//    }
//
//    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
//    }
//
//    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
//        // TODO Auto-generated method stub
//    }
//
//}