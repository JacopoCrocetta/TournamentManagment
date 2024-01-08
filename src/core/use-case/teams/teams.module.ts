import { Module } from "@nestjs/common";
import { TeamsCreate } from "./teams-create";
import { DeleteTeam } from "./teams-delete";
import { TeamsPage } from "./teams-page";
import { TeamUpdate } from "./teams-update";

const providers = [TeamsCreate, DeleteTeam, TeamsPage, TeamUpdate];

@Module({ providers, exports: { ...providers } })
export class TeamsModule{}