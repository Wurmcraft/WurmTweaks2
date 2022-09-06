package com.wurmcraft.wurmtweaks2.common.command;

import com.wurmcraft.wurmtweaks2.common.script.ScriptRunner;
import java.io.StringWriter;
import java.io.Writer;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;


public class InterpreterCommand extends CommandBase {

  @Override
  public String getName() {
    return "runScript";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/runScript <script>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
      return;
    }
    Writer writer = new StringWriter();
    try {
      ScriptRunner.createBindings();
      ScriptContext context = new SimpleScriptContext();
      context.setBindings(ScriptRunner.createBindings(), ScriptContext.GLOBAL_SCOPE);
      context.setBindings(ScriptRunner.createBindings(), ScriptContext.ENGINE_SCOPE);
      context.setAttribute(ScriptEngine.FILENAME, "player.py",
          ScriptContext.ENGINE_SCOPE);
      context.setAttribute("mc_version", "1.12.2", ScriptContext.GLOBAL_SCOPE);
      context.setWriter(writer);
      ScriptRunner.engine
          .eval(Strings.join(ScriptRunner.core_py, "") + Strings.join(args, " "),
              context);
    } catch (ScriptException e) {
      sender.sendMessage(new TextComponentString(e.getMessage()));
    }
    sender.sendMessage(new TextComponentString(writer.toString()));
  }
}
