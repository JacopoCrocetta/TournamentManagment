import { Module } from "@nestjs/common";
import { TournamentController } from "./tournament.controller";

const controllers = [TournamentController];

@Module({
  controllers,
  exports: [...controllers]
})
export class ControllerModule {}
