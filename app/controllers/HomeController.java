package controllers;

import play.api.http.MediaRange;
import play.mvc.*;
import play.api.libs.EventSource;
import play.api.libs.iteratee.Concurrent;
import play.api.libs.json.JsValue;
import play.mvc.Controller;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    val(chatOut, chatChannel) =Concurrent.broadcast[JsValue];

    def index = Action

    {
        implicit req =>
        ok(views.html.index(routes.Application.chatFeed(), routes.HomeController.postMessage))

    }

    def chatFeed = Action

    {
        reg =>
        println("Someone connected" + req.remoteAddress)
        ok.chunked(chatOut & > EventSource()
        ).as("text/event-stream")

        def postMessage = Action(MediaRange.parse.json) {
        req =>chatChannel.push(reg.body);