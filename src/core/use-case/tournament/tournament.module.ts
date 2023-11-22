import { Module } from "@nestjs/common";
import { TournamentCreate } from "./tournament-create";
import { DeleteTournament } from "./tournament-delete";
import { TournamentPage } from "./tournament-page";
import { TournamentUpdate } from "./tournament-update";


const providers = [TournamentCreate, DeleteTournament, TournamentPage, TournamentUpdate];

@Module({ providers, exports: [...providers] })
export class TournamentsModule {}
