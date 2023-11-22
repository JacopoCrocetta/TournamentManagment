import { Inject, Injectable, Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

@Injectable()
export class MatchCreate {
  constructor(@Inject() private matchRepository: IMatchRepository) {}

  create = async (matchToCreate: Match): Promise<Match> => {
    Logger.log("CREATING NEW MATCH...");
    const createdMatch = await this.matchRepository.create(matchToCreate);
    
    Logger.log("CHECKING IF THE MATCH WAS CREATED");
    if(!!createdMatch){
      Logger.log("MATCH WAS NOT CREATED CORRECTLY");
      return null;
    }

    Logger.log("MATCH CREATED CORRECTLY");
    return createdMatch;
  };
}
