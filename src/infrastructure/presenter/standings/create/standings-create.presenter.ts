import { Logger } from "@nestjs/common";
import { Standings } from "src/core/entity/abstract-standings";
import { IStandingRepository } from "src/core/repository/standings.repository";

export class StandingsCreatePresenter {
    constructor(private repository: IStandingRepository){}

    async createStanding(newStanding: Standings): Promise<Standings>{
        Logger.log("TRYING TO CREATE A NEW STANDING");
        return this.repository.create(newStanding);
    }
}