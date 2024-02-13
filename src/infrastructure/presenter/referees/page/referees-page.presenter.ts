import { Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { IRefereesRepository } from "src/core/repository/referees.repository";

export class RefereesPagePresenter{
    constructor(private repository: IRefereesRepository){}


    async getRefereesById(id: number): Promise<Referees>{
        Logger.log("RETREIVING REFEREES WITH ID " + id);
        return this.repository.getById(id);
    }

    async getAllMatch():Promise<Referees[]>{
        Logger.log("RETREIVING ALL MTCH FOR REFEREES WITH ID ");
        return this.repository.getAll();
    }
}