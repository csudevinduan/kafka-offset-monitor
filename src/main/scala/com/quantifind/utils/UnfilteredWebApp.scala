package com.quantifind.utils

import com.quantifind.sumac.{ArgMain, FieldArgs}
import com.quantifind.utils.UnfilteredWebApp.Arguments
import unfiltered.util.Port

/**
 * build up a little web app that serves static files from the resource directory
 * and other stuff from the provided plan
 * User: pierre
 * Date: 10/3/13
 */
trait UnfilteredWebApp[T <: Arguments] extends ArgMain[T] {

  def htmlRoot: String

  def setup(args: T): unfiltered.filter.Plan

  def afterStart() {}

  def afterStop() {}

  override def main(parsed: T) {
    val root = getClass.getResource(htmlRoot)
    println("serving resources from: " + root)
    val resource =  unfiltered.jetty.Http(parsed.port).resources(root)
    resource.current.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed","false")
    resource.filter(setup(parsed))
    resource.run(_ => afterStart(), _ => afterStop())
  }
}

object UnfilteredWebApp {

  trait Arguments extends FieldArgs {
    var port = Port.any
  }

}
