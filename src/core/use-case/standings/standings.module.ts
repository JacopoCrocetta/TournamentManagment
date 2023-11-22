import { Module } from "@nestjs/common";
import { StandingsCreate } from "./standings-create";
import { StandingsUpdate } from "./standings-update";

const providers = [StandingsCreate, StandingsUpdate];

@Module({ providers, exports: { ...providers } })
export class StandingsModule{}