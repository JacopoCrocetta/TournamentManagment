import { Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { Standings } from "src/core/entity/abstract-standings";
import { IRefereesRepository } from "src/core/repository/referees.repository";
import { IStandingRepository } from "src/core/repository/standings.repository";

export class StandingsUpdatePresenter {
    constructor(private repository: IStandingRepository){}

    async updateStanding(standingToUpdate: Standings):Promise<Standings>{
        Logger.log("TRYING TO UPDATE THE STANDING WITH ID " + standingToUpdate.id);

        const standingUpdated = await this.repository.update(standingToUpdate);

        Logger.log("STANDING WITH ID " + standingUpdated.id+ " IS UPDATED");
        return standingUpdated;
    }
}