package cc.sven.android.dooris

import android.appwidget._
import android.util.Log
import android.content._
import android.widget.RemoteViews
import java.io._
import java.net._
import scala.io.Source

class DoorisWidget extends AppWidgetProvider {
  Log.i("Dooris", "Widget instantiated")

  override def onUpdate(context : Context, appWidgetManager : AppWidgetManager, appWidgetIds : Array[Int]) {
    Log.i("Dooris", "Widget updated?")
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    val appWidgetIdsList = appWidgetIds toList

    val url = new URL("http://dooris.koalo.de/door.txt")
    val uis = url.openConnection.getInputStream
    val string = Source.fromInputStream(uis).mkString
    Log.i("Dooris", string)

    def updateWidget(id : Int) {
      Log.i("Dooris", "ID: " + id toString)
      val thisWidget = new ComponentName(context, classOf[DoorisWidget])
      val rView = new RemoteViews(context.getPackageName(), R.layout.widget)
      rView.setTextViewText(R.id.text, string)
      appWidgetManager.updateAppWidget(thisWidget, rView)
    }
    appWidgetIdsList.foreach(updateWidget)
  }
}
