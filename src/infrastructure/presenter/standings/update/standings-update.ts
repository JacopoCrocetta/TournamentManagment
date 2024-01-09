import { Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

export class StandingsUpdatePresenter {
    constructor(private repository: IRefereesRepository){}

    async updateMatchById(refereesToUpdate: Referees):Promise<Referees>{
        Logger.log("TRYING TO UPDATE THE REFEREES WITH ID " + refereesToUpdate.id);

        const refereesUpdated = await this.repository.update(refereesToUpdate);

        Logger.log("REFEREES WITH ID " + refereesUpdated.id+ " IS UPDATED");
        return refereesUpdated;
    }
}