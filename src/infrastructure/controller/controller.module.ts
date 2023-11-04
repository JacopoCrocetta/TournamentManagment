import { Module } from "@nestjs/common";
import { AppController } from "./healtz.controller";
import { TournamentController } from "./tournament.controller";

const controllers = [AppController, TournamentController];

@Module({
  controllers,
})
export class ControllerModule {}
