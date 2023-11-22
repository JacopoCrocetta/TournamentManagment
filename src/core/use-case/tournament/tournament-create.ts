import { Inject, Injectable, Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

@Injectable()
export class TournamentCreate {
  constructor(@Inject() private tournamentRepository: ITournamentsRepository) {}

  create = async (input: Tournament) => {
    Logger.log("CREATING A NEW TOURNAMENT...");
    const createdTournament = this.tournamentRepository.create(input);

    Logger.log("CHECKING IF THE TOURNAMENT WAS CREATED");
    if(!!createdTournament){
      Logger.log("TOURNAMENT WAS NOT CREATED CORRECTLY");
      return null;
    }

    Logger.log("TOURNAMENT CREATED CORRECTLY");
    return createdTournament;
  };
}
