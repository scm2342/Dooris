package cc.sven.android.dooris

import android.appwidget._
import android.util.Log
import android.content._
import android.widget.RemoteViews
import java.io._
import java.net._
import scala.io.Source
import scala.collection.immutable.Stream

class DoorisWidget extends AppWidgetProvider {
  override def onUpdate(context : Context, appWidgetManager : AppWidgetManager, appWidgetIds : Array[Int]) {
    Log.i("Dooris", "Widget update requested")
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    val appWidgetIdsList = appWidgetIds toList

    try {
      val url = new URL("http://dooris.koalo.de/door.txt")
      val uis = url.openStream
      val stream = Stream.continually(uis.read).take(100).takeWhile(_ != -1).map((i) => i toChar).takeWhile(_ != '\n')
      setStr(stream.mkString)
      uis.close
    } catch {
      case e : IOException => setStr(e.getMessage)
    }

    def updateWidget(str: String, id : Int) {
      Log.i("Dooris", "updating id " + id toString)
      val thisWidget = new ComponentName(context, classOf[DoorisWidget])
      val rView = new RemoteViews(context.getPackageName(), R.layout.widget)
      rView.setTextViewText(R.id.text, str)
      appWidgetManager.updateAppWidget(thisWidget, rView)
    }
    def setStr(string : String) = appWidgetIdsList.foreach(updateWidget(string, _))
  }
}
