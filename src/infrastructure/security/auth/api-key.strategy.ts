import {PassportStrategy} from '@nestjs/passport';
import {Injectable} from '@nestjs/common';
import {ConfigService} from "@nestjs/config";
import Strategy from "passport-headerapikey";

const getStrategyConfiguration = (_apiKey: string)  => {
    const header = {header: 'platform-svc-api-key', prefix: ''};
    const passReqToCallback = false;
    const verify = (
        apiKey: string,
        verified: (error: Error | null, done?: boolean) => void
    ): void =>  verified(null, (apiKey === _apiKey));
    return [ header, passReqToCallback, verify ];
}

@Injectable()
export class ApiKeyStrategy extends PassportStrategy(Strategy, 'apiKey') {

    constructor(private configService: ConfigService) {
        const strategyConfig = getStrategyConfiguration(configService.get('PLATFORM_SVC_API_KEY'));
        super(...strategyConfig);
    }
}
