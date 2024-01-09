import { Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

export class RefereesCreatePresenter {
    constructor(private repository: IRefereesRepository){}

    async createMatchById(newRef: Referees): Promise<Referees>{
        Logger.log("TRYING TO CREATE A NEW REFEREES ");
        return this.repository.create(newRef);
    }
}