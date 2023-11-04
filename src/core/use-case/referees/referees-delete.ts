import { Inject, Injectable } from "@nestjs/common";
import { IRefereesRepository } from "src/core/repository/referees.repository";

@Injectable()
export class RefereesDelete{
    constructor(@Inject() private refRepository: IRefereesRepository){}

    delete =async (id:number):Promise<boolean> => {
        this.refRepository.delete(id);

        return this.refRepository.getById(id) === null;
    }
}