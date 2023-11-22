import { Module } from "@nestjs/common";
import { MatchModule } from "./match/match.module";
import { TournamentsModule } from "./tournament/tournament.module";
import { PartecipantModule } from "./partecipant/partecipant.module";
import { RefereesModule } from "./referees/referees.module";
import { UserModule } from "./user/user.module";

const providers = [MatchModule, TournamentsModule, PartecipantModule, RefereesModule, UserModule];
@Module({ providers, exports: [...providers] })
export class UseCasesModule {}
